import { UserSession } from "../common/types/user";
import { CommentType } from "../common/types/comment";
import React from "react";
import { Dropdown, Grid, Menu } from "semantic-ui-react";

export function CommentDetails({
  comment,
  handleDelete,
  handleEdit,
  user,
}: {
  comment: CommentType;
  handleDelete(): void;
  handleEdit(): void;
  user: UserSession;
}) {
  return (
    <div>
      <Menu
        vertical
        color="blue"
        inverted
        style={{
          display:
            user.userID == comment.entities[0].properties.user_id
              ? "visible"
              : "none",
        }}
      >
        <Dropdown item text="Actions" color="blue">
          <Dropdown.Menu>
            <Dropdown.Item onClick={handleDelete}>Delete Comment</Dropdown.Item>
            <Dropdown.Item onClick={handleEdit}>Edit Comment</Dropdown.Item>
          </Dropdown.Menu>
        </Dropdown>
      </Menu>
      <h2>Comment Text</h2>
      <Grid columns={8}>
        <Grid.Row>
          <Grid.Column>
            <Menu fluid vertical>
              <Menu.Item style={{ textAlign: "center" }} className="header">
                {comment.properties.comment_text}
              </Menu.Item>
            </Menu>
          </Grid.Column>
        </Grid.Row>
      </Grid>
      <h2>Creation Date</h2>
      <Grid columns={8}>
        <Grid.Row>
          <Grid.Column>
            <Menu fluid vertical>
              <Menu.Item style={{ textAlign: "center" }} className="header">
                {new Date(comment.properties.creation_date * 1000).toString()}
              </Menu.Item>
            </Menu>
          </Grid.Column>
        </Grid.Row>
      </Grid>
    </div>
  );
}
