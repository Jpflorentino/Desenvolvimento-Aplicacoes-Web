import { UserSession } from "../common/types/user";
import { CommentType } from "../common/types/comment";
import { Comment } from "./comment";
import React, { useEffect, useState, useContext } from "react";
import { Redirect } from "react-router-dom";
import { NavbarContext } from "../navbar/navbarContext";

export function EditComment({
  comment,
  uri,
  projectId,
  userID,
  user,
}: {
  comment: CommentType;
  uri: string;
  projectId: string;
  userID: string;
  user: UserSession;
}) {
  if (!user.isLoggedIn) return <Redirect to="/loginRegister" />;

  const [cancelState, setCancelState] = useState(false);
  const [newText, setNewText] = useState(comment.properties.comment_text);
  const [submit, setSubmit] = useState(false);

  /// Update navbar active item
  const { activeItem, updateActiveItem } = useContext(NavbarContext);
  useEffect(() => {
    /// Avoid component side-effects
    if (activeItem !== "projects") updateActiveItem("projects");
  }, []);

  let body = {
    comment_text: newText || comment.properties.comment_text,
  };

  useEffect(() => {
    if (!submit) return;

    setSubmit(false);

    const options = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + user.authCredentials,
      },
      body: JSON.stringify(body),
    };

    fetch(
      uri
        .replace("{userid}", userID)
        .replace("{projectid}", projectId)
        .replace("{issueid}", comment.properties.issue_id.toString())
        .replace("{commentid}", comment.properties.comment_id.toString()),
      options
    )
      .then((resp) => {
        if (resp.status > 300) return resp.json(); //to handle the errors
      })
      .then((json) => {
        if (json) {
          alert(json.detail);
        } else {
          handleCancel();
        }
      })
      .catch((err) => alert(err));
  }, [submit]);

  function handleCommentEditSubmit() {
    setSubmit(true);
  }

  function handleCancel() {
    setCancelState(true);
  }

  if (cancelState) {
    return <Comment uri={uri} />;
  }

  return (
    <div className="ui form">
      <div className="fields">
        <div className="required field">
          <label>New Text</label>
          <input
            type="text"
            name="name"
            defaultValue={comment.properties.comment_text}
            onChange={(event) => {
              setNewText(event.target.value);
            }}
          />
        </div>
      </div>
      <button
        className="ui button"
        type="submit"
        onClick={handleCommentEditSubmit}
      >
        Edit!
      </button>
      <button className="negative ui button" onClick={handleCancel}>
        Cancel
      </button>
    </div>
  );
}
