//Types for useFetch regarding State, Action and Body of request

export type State =
  | {
      type: "started";
    }
  | {
      type: "fetching";
    }
  | {
      type: "error";
      error: string;
    }
  | {
      type: "response";
      status: number;
      body: string;
    };

export type Action =
  | {
      type: "fetching";
    }
  | {
      type: "error";
      error: string;
    }
  | {
      type: "response";
      status: number;
      body: string;
    };

export type MyBody = {
  username: string;
  password: string;
};

export type Method = "POST" | "PUT" | "DELETE" | "GET";

//request options for useFetch
export type Options = {
  signal: AbortSignal;
  method: string;
  headers: Headers | undefined;
  body: string | undefined;
};

export type Headers = {
  Authorization: string;
};

export type ProjectRouteParams = {
  user_id: string;
  project_id: string;
};

export type IssueRouteParams = {
  user_id: string;
  project_id: string;
  issue_id: string;
};

export type EditProjectRouteParams = {
  project_id: string;
};

export type CommentRouteParams = {
  user_id: string;
  project_id: string;
  issue_id: string;
  comment_id: string;
};
