import React, { useEffect, useState, useContext } from "react";
import { Redirect } from "react-router-dom";
import { Issue } from "../issues/issue";
import { IssueType } from "../common/types/issue";
import { NavbarContext } from "../navbar/navbarContext";
import { UserSessionContext} from "../users/userSessionContext";

/// EditLabels component props
type EditLabelseProps = {
  uri: string;
  issue: IssueType;
  changeLabelURI: string;
}

export function EditLabels({
  uri,
  issue,
  changeLabelURI,
}: EditLabelseProps ) {

  /// If user is not logged in, redirect to Login / Register page
  const { activeUserSession } = useContext(UserSessionContext);
  if (!activeUserSession.isLoggedIn) return <Redirect to="/loginRegister" />;

  /// States
  const [cancelState, setCancelState] = useState(false);
  const [newLabel, setNewLabel] = useState("");
  const [httpMethod, setHttpMethod] = useState("");
  const [submit, setSubmit] = useState(false);

  /// Update navbar active item
  const { activeItem, updateActiveItem } = useContext(NavbarContext);
  useEffect(() => { /// Avoid component side-effects
    if(activeItem !== "projects") updateActiveItem("projects");
  }, [])

  let body = {
    label: newLabel,
  };

  /// labels edition
  useEffect(() => {
    if (!submit) return;

    setSubmit(false);

    const options = {
      method: httpMethod,
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
      body: JSON.stringify(body),
    };

    fetch(
      changeLabelURI
        .replace("{userid}", activeUserSession.userID.toString())
        .replace("{projectid}", issue.properties.project_id.toString())
        .replace("{issueid}", issue.properties.issue_id.toString()),
      options
    )
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
  function handleLabelSubmit(method: string) {
    if (newLabel == "") {
      alert("Label field can't be empty");
    } else {
      setHttpMethod(method);
      setSubmit(true);
    }
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
    return <Issue uri={uri}/>;
  }

  return (
    <div className="ui form">
      <div className="fields">
        <div className="required field">
          <label>New Label</label>
          <input
            type="text"
            name="name"
            placeholder="The name of the label"
            onChange={(event) => {
              setNewLabel(event.target.value); //since the fields are already filled, we can use the value prop (unless this fucks up bc we are using the value prop and it triggers on change)
            }}
          />
        </div>
        <button
          className="ui button"
          type="submit"
          onClick={() => handleLabelSubmit("POST")}
        >
          Add Label!
        </button>
        <button
          className="ui button"
          type="submit"
          onClick={() => handleLabelSubmit("DELETE")}
        >
          Remove Label!
        </button>
        <button className="negative ui button" onClick={handleCancel}>
          Cancel
        </button>
      </div>
    </div>
  );
}
