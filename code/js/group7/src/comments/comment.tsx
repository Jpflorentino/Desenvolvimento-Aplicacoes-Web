import React, { useState, useEffect, useContext } from "react";
import { Redirect, useParams } from "react-router-dom";
import { CommentRouteParams } from "../common/types/utils";
import { useHistory } from "react-router-dom";
import { CommentType } from "../common/types/comment";
import { CommentDetails } from "./commentDetails";
import { EditComment } from "./editComment";
import { NavbarContext } from "../navbar/navbarContext";
import { UserSessionContext } from "../users/userSessionContext";

export function Comment({ uri }: { uri: string }) {
  let { user_id, project_id, issue_id, comment_id } =
    useParams<CommentRouteParams>();

  const { activeUserSession } = useContext(UserSessionContext);
  if (!activeUserSession.isLoggedIn) return <Redirect to="/loginRegister" />;

  /// States
  const [comment, setComment] = useState<CommentType>(null);
  const [deleteState, setDeleteState] = useState(false);
  const [editState, setEditState] = useState(false);

  const history = useHistory();

  /// Update navbar active item
  const { activeItem, updateActiveItem } = useContext(NavbarContext);
  useEffect(() => {
    /// Avoid component side-effects
    if (activeItem !== "projects") updateActiveItem("projects");
  }, []);

  /*
  Get the comment Details
  */
  useEffect(() => {
    if (comment) return;

    const options = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
    };

    fetch(
      uri
        .replace("{userid}", user_id)
        .replace("{projectid}", project_id)
        .replace("{issueid}", issue_id)
        .replace("{commentid}", comment_id),
      options
    )
      .then((resp) => {
        return resp.json();
      })
      .then((json) => {
        if (json.status == null) setComment(json);
        else alert(json.detail);
      })
      .catch((err) => alert("There was a problem parsing the comment Details"));
  }, []);

  /*
  Delete the comment
  */
  useEffect(() => {
    if (deleteState == false) {
      return;
    }

    const deleteLink = comment.actions.find(
      (element) => element.name === "delete-comment"
    ).href;

    const options = {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
    };

    fetch(deleteLink, options)
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
      .catch((err) => alert("There was a problem deleting the comment"));
  }, [deleteState]);

  function handleDelete() {
    if (activeUserSession.userID != comment.entities[0].properties.user_id)
      alert("You can't delete other people's comments");
    else setDeleteState(true);
  }

  function handleEdit() {
    if (activeUserSession.userID != comment.entities[0].properties.user_id)
      alert("You can't edit other people's comments");
    else setEditState(true);
  }

  if (editState) {
    return (
      <EditComment
        user={activeUserSession}
        uri={uri}
        projectId={project_id} //the comment object doesn't have a property with it's project
        userID={user_id} //the user_id property of the comment object is its owner, not the owner of the project
        comment={comment}
      />
    );
  }

  if (!comment) {
    return <div>empty</div>;
  } else {
    return (
      <div style={{ margin: "10px 10px 10px 10px" }}>
        <CommentDetails
          comment={comment}
          handleDelete={handleDelete}
          handleEdit={handleEdit}
          user={activeUserSession}
        />
      </div>
    );
  }
}
