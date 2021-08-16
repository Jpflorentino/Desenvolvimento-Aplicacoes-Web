import { useEffect, useReducer } from "react";
import { State, Action, Method, Options } from "../types/utils";

function actionError(error: string): Action {
  return { type: "error", error: error };
}

function actionResponse(status: number, body: string): Action {
  return { type: "response", status: status, body: body };
}

function reducer(state: State, action: Action): State {
  switch (action.type) {
    case "fetching":
      return { type: "fetching" };
    case "error":
      return { type: "error", error: action.error };
    case "response":
      return { type: "response", status: action.status, body: action.body };
  }
}

export function useFetch(
  uri: string,
  method: Method,
  options: Options,
  userCredsBase64: string | undefined
): State {
  const [state, dispatcher] = useReducer(reducer, { type: "started" });

  useEffect(() => {
    if (!uri) {
      return;
    }

    let isCancelled = false;
    const abortController = new AbortController();
    const signal = abortController.signal;

    async function doFetch() {
      try {

        dispatcher({ type: "fetching" });
        const resp = await fetch(uri, options);
        if (isCancelled) {
          return;
        }

        const body = await resp.text();
        if (isCancelled) {
          return;
        }
        
        dispatcher(actionResponse(resp.status, body));
      } catch (error) {
        if (isCancelled) {
          return;
        }
        dispatcher(actionError(error.message));
      }
    }

    doFetch();
    return () => {
      isCancelled = true;
      abortController.abort();
    };
  }, [uri, method, options, userCredsBase64]);

  return state;
}
