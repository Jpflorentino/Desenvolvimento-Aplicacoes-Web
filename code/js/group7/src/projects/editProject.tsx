import React, { useContext, useEffect, useState } from "react";
import { ProjectType } from "../common/types/project";
import { Redirect } from "react-router-dom";
import { Project } from "./project";
import { NavbarContext } from "../navbar/navbarContext";
import { UserSessionContext } from "../users/userSessionContext";

/// ProjectDisplay component props
type EditProjectProps = {
  uri: string;
  project: ProjectType;
};

export function EditProject({ uri, project }: EditProjectProps) {
  /// If user is not logged in, redirect to Login / Register page
  const { activeUserSession } = useContext(UserSessionContext);
  if (!activeUserSession.isLoggedIn) return <Redirect to="/loginRegister" />;

  const [cancelState, setCancelState] = useState(false); //state used to render the edit or details components of the project

  const [newName, setNewName] = useState(project.properties.name);
  const [newDescription, setNewDescription] = useState(project.properties.description);
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
    allowed_labels_set: newLabels,
    allowed_states_set: newStates,
    allowed_transitions_set: newTransitions,
  };

  /// Edit project
  useEffect(() => {
    if (!submit) return;

    setSubmit(false);

    const options = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
      body: JSON.stringify(body),
    };

    fetch(uri.replace("{userid}", activeUserSession.userID.toString()).replace("{projectid}", project.properties.project_id.toString()), options)
      .then((resp) => {
        if (resp.status > 300) return resp.json(); //to handle the errors
      })
      .then((json) => {
        if (json) {
          alert(json.detail);
        } else {
          handleCancel(); //since we don't use the router to browse between project edit/details, we need to use this function to go back to the project page
        }
      })
      .catch((err) => alert(err));
  }, [submit]);

  /**
   * Handlers
   */
  function handleProjectEditSubmit() {
    if (body.name == "") {
      setNewName(project.properties.name);
    }

    if (body.description == "") {
      setNewDescription(project.properties.description);
    }

    if (body.allowed_labels_set == "") {
      delete body.allowed_labels_set;
    } else {
      setNewLabels(`[${newLabels.replace(" ", "")}]`);
    }

    if (body.allowed_states_set == "") {
      delete body.allowed_states_set;
    } else {
      setNewStates(`[${newStates.replace(" ", "")}]`);
    }

    if (body.allowed_transitions_set == "") {
      delete body.allowed_transitions_set;
    } else {
      let validTransitions = verifyTransitions(body.allowed_transitions_set, body.allowed_states_set || project.properties.states.join(","));

      if (validTransitions) {
        setNewTransitions(`[${newTransitions}]`);
      } else {
        delete body.allowed_transitions_set;
        alert("Invalid transition: All transitions need to exist in the allowed states field");
      }
    }
    setSubmit(true);
  }

  function handleCancel() {
    setCancelState(true);
  }

  /*
  Since we need to pass the project content to the Edit project page, in order to avoid
  another request to the API, we can render the EditProject component and the Project component
  in the "same" page

  In this case, when the user clicks "Cancel" we render the project component and pass through the props
  all the data required to re-render the project details
  */
  if (cancelState) {
    return <Project uri={uri} />;
  }

  return (
    <div className="ui form">
      <div className="fields">
        <div className="required field">
          <label>New Name</label>
          <input
            type="text"
            name="name"
            defaultValue={project.properties.name}
            onChange={(event) => {
              setNewName(event.target.value); //since the fields are already filled, we can use the value prop (unless this fucks up bc we are using the value prop and it triggers on change)
            }}
          />
        </div>
        <div className="required field">
          <label>New Description</label>
          <input
            type="text"
            name="description"
            defaultValue={project.properties.description}
            onChange={(event) => {
              setNewDescription(event.target.value);
            }}
          />
        </div>
        <div className="field">
          <label>New Labels</label>
          <input
            type="text"
            name="labels"
            placeholder="label1,label2,..."
            onChange={(event) => {
              setNewLabels(event.target.value);
            }}
          />
        </div>
        <div className="field">
          <label>New States</label>
          <input
            type="text"
            name="states"
            placeholder="state1,state2,..."
            onChange={(event) => {
              setNewStates(event.target.value);
            }}
          />
          <div className="field">
            <label>New Transitions</label>
            <input
              type="text"
              name="transitions"
              placeholder="state1:state2,state3:state4,..."
              onChange={(event) => {
                setNewTransitions(event.target.value);
              }}
            />
          </div>
        </div>
      </div>
      <button className="ui button" type="submit" onClick={handleProjectEditSubmit}>
        Edit!
      </button>
      <button className="negative ui button" onClick={handleCancel}>
        Cancel
      </button>
    </div>
  );
}

function verifyTransitions(transitions: string, states: string): boolean {
  let isolated = transitions.replace("[", "").replace("]", "").replace(" ", "").split(","); //[a:b,c:d,e:f]

  let isolatedStates = states.replace("[", "").replace("]", "").replace(" ", "").split(",");

  for (let i = 0; i < isolated.length; i++) {
    let state1 = isolated[i].split(":")[0];
    let state2 = isolated[i].split(":")[1];

    if (!isolatedStates.find((state) => state == state1) || !isolatedStates.find((state) => state == state2)) {
      return false;
    }
  }

  return true;
}
