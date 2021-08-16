import React, { useEffect, useState, useContext } from "react";
import { useHistory, Redirect } from "react-router-dom";
import { NavbarContext } from "../navbar/navbarContext";
import { UserSessionContext } from "../users/userSessionContext";

/// CreateProject component props
type CreateProjectProps = {
  uri: string;
};

export function CreateProject({ uri }: CreateProjectProps) {
  /// If user is not logged in, redirect to Login / Register page
  const { activeUserSession } = useContext(UserSessionContext);
  if (!activeUserSession.isLoggedIn) return <Redirect to="/loginRegister" />;

  /// useHistory hook
  const history = useHistory();

  /// useState hook
  const [newName, setNewName] = useState("");
  const [newDescription, setNewDescription] = useState("");
  const [newLabels, setNewLabels] = useState("");
  const [newStates, setNewStates] = useState("");
  const [newTransitions, setNewTransitions] = useState("");
  const [submit, setSubmit] = useState(false);

  /// Update navbar active item
  const { activeItem, updateActiveItem } = useContext(NavbarContext);
  useEffect(() => {
    /// Avoid component side-effects
    if (activeItem !== "projects") updateActiveItem("projects");
  }, []);

  let body = {
    name: newName,
    description: newDescription,
    allowed_labels_set: `[${newLabels.replace(" ", "")}]`, //needs input verification,
    allowed_states_set: `[${newStates.replace(" ", "")}]`,
    allowed_transitions_set: `[${newTransitions}]`,
  };

  // Requests project creation
  useEffect(() => {
    if (!submit) return;

    setSubmit(false);

    const options = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
      body: JSON.stringify(body),
    };

    fetch(
      uri
        .replace("{userid}", activeUserSession.userID.toString())
        .replace("/{projectid}", ""),
      options
    )
      .then((resp) => {
        if (resp.status > 300) return resp.json(); //to handle the errors
      })
      .then((json) => {
        if (json) {
          alert(json.detail);
        } else {
          history.push("/user");
        }
      })
      .catch((err) => alert(err));
  }, [submit]);

  /**
   * Handlers
   */
  function handleProjectCreateSubmit() {
    if (body.name == "" || body.description == "") {
      alert("The project has to have a name and description!");
    } else if (
      body.allowed_transitions_set == "[]" &&
      body.allowed_states_set == "[]"
    ) {
      setSubmit(true);
    } else {
      let validTransitions = verifyTransitions(
        body.allowed_transitions_set,
        body.allowed_states_set
      );

      if (validTransitions) setSubmit(true);
      else
        alert(
          "Invalid transition: All transitions need to exist in the allowed states field"
        );
    }
  }

  function handleCancel() {
    history.push("/user");
  }

  return (
    <div className="ui form">
      <div className="fields">
        <div className="required field">
          <label>Name</label>
          <input
            type="text"
            name="name"
            placeholder="The project's name"
            onChange={(event) => {
              setNewName(event.target.value); //since the fields are already filled, we can use the value prop (unless this fucks up bc we are using the value prop and it triggers on change)
            }}
          />
        </div>

        <div className="required field">
          <label>Description</label>
          <input
            type="text"
            name="description"
            placeholder="The project's description"
            onChange={(event) => {
              setNewDescription(event.target.value);
            }}
          />
        </div>

        <div className="field">
          <label>Labels</label>
          <input
            type="text"
            name="labels"
            placeholder="format: label1,label2"
            onChange={(event) => {
              setNewLabels(event.target.value);
            }}
          />
        </div>

        <div className="field">
          <label>States (To be attributed to the project's issues)</label>
          <input
            type="text"
            name="states"
            placeholder="format: state1,state2"
            onChange={(event) => {
              setNewStates(event.target.value);
            }}
          />

          <div className="field">
            <label>
              Transitions (Possible changes of state for the project's issues)
            </label>
            <input
              type="text"
              name="transitions"
              placeholder="format: stateA:stateB,stateC:stateD"
              onChange={(event) => {
                setNewTransitions(event.target.value);
              }}
            />
          </div>
        </div>
      </div>

      <button
        className="ui button"
        type="submit"
        onClick={handleProjectCreateSubmit}
      >
        Create a project!
      </button>

      <button className="negative ui button" onClick={handleCancel}>
        Cancel
      </button>
    </div>
  );
}

function verifyTransitions(transitions: string, states: string): boolean {
  let isolated = transitions
    .replace("[", "")
    .replace("]", "")
    .replace(" ", "")
    .split(","); // [a:b,c:d,e:f]

  let isolatedStates = states
    .replace("[", "")
    .replace("]", "")
    .replace(" ", "")
    .split(",");

  for (let i = 0; i < isolated.length; i++) {
    const state1 = isolated[i].split(":")[0];
    const state2 = isolated[i].split(":")[1];

    if (
      !isolatedStates.find((state) => state == state1) ||
      !isolatedStates.find((state) => state == state2)
    )
      return false;
  }

  return true;
}
