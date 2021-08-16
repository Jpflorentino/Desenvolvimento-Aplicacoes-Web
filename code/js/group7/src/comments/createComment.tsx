import React, { useEffect, useState, useContext } from "react";
import { useHistory, Redirect, useParams } from "react-router-dom";
import { IssueRouteParams } from "../common/types/utils";
import { NavbarContext } from "../navbar/navbarContext";
import { UserSessionContext } from "../users/userSessionContext";

export function CreateComment({ uri }: { uri: string }) {
  let { user_id, project_id, issue_id } = useParams<IssueRouteParams>();

  const { activeUserSession } = useContext(UserSessionContext);
  if (!activeUserSession.isLoggedIn) return <Redirect to="/loginRegister" />;

  const history = useHistory();

  const [newComment, setNewComment] = useState("");
  const [submit, setSubmit] = useState(false);

  /// Update navbar active item
  const { activeItem, updateActiveItem } = useContext(NavbarContext);
  useEffect(() => {
    /// Avoid component side-effects
    if (activeItem !== "projects") updateActiveItem("projects");
  }, []);

  let body = {
    comment_text: newComment,
  };

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
        .replace("{userid}", user_id)
        .replace("{projectid}", project_id)
        .replace("{issueid}", issue_id)
        .replace("/{commentid}", ""),
      options
    )
      .then((resp) => {
        if (resp.status > 300) return resp.json(); //to handle the errors
      })
      .then((json) => {
        if (json) {
          alert(json.detail);
        } else {
          history.push(`/${user_id}/projects/${project_id}/issues/${issue_id}`);
        }
      })
      .catch((err) => alert(err));
  }, [submit]);

  function handleCommentCreateSubmit() {
    if (body.comment_text == "") {
      alert("The comment must have text");
    } else {
      setSubmit(true);
    }
  }

  function handleCancel() {
    history.push(`/${user_id}/projects/${project_id}/issues/${issue_id}`);
  }

  return (
    <div className="ui form">
      <div className="fields">
        <div className="required field">
          <label>New Text</label>
          <input
            type="text"
            name="name"
            placeholder="The comment's text"
            onChange={(event) => {
              setNewComment(event.target.value); //since the fields are already filled, we can use the value prop (unless this fucks up bc we are using the value prop and it triggers on change)
            }}
          />
        </div>
      </div>
      <button
        className="ui button"
        type="submit"
        onClick={handleCommentCreateSubmit}
      >
        Create a comment!
      </button>
      <button className="negative ui button" onClick={handleCancel}>
        Cancel
      </button>
    </div>
  );
}
