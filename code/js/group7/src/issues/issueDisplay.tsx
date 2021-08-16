import React, { useContext } from "react";
import { IssueType } from "../common/types/issue";
import { Dropdown, Grid, Menu } from "semantic-ui-react";
import { UserSessionContext } from "../users/userSessionContext";

/// IssueDisplay component props
type IssueDisplayProps = {
  issue: IssueType;
  handleDelete(): void;
  handleEditIssueClick(): void;
  handleEditLabelClick(): void;
  handleEditStateClick(): void;
  handleCommentCreate(): void;
};

export function IssueDisplay({
  issue,
  handleDelete,
  handleEditIssueClick,
  handleEditLabelClick,
  handleEditStateClick,
  handleCommentCreate,
}: IssueDisplayProps) {
  const { activeUserSession } = useContext(UserSessionContext);

  return (
    <div>
      <Menu vertical color="blue" inverted>
        <Dropdown item text="Actions" color="blue">
          <Dropdown.Menu>
            <Dropdown.Item
              onClick={handleDelete}
              style={{
                display:
                  activeUserSession.userID == issue.properties.user_id
                    ? "visible"
                    : "none",
              }}
            >
              Delete Issue
            </Dropdown.Item>
            <Dropdown.Item onClick={handleCommentCreate}>
              Create Comment
            </Dropdown.Item>
          </Dropdown.Menu>
        </Dropdown>
      </Menu>
      <h2>Name</h2>
      <Grid columns={8}>
        <Grid.Row>
          <Grid.Column>
            <Menu fluid vertical>
              <Menu.Item style={{ textAlign: "center" }} className="header">
                {issue.properties.name}
              </Menu.Item>
            </Menu>
          </Grid.Column>
        </Grid.Row>
      </Grid>
      <h2>Description</h2>
      <Grid columns={8}>
        <Grid.Row>
          <Grid.Column>
            <Menu fluid vertical>
              <Menu.Item style={{ textAlign: "center" }} className="header">
                {issue.properties.description}
              </Menu.Item>
            </Menu>
          </Grid.Column>
        </Grid.Row>
      </Grid>
      <button
        className="ui primary button"
        onClick={handleEditIssueClick}
        style={{
          visibility:
            activeUserSession.userID == issue.properties.user_id
              ? "visible"
              : "hidden",
        }}
      >
        Edit Issue
      </button>
      <h2>Labels</h2>
      <Grid columns={4}>
        <Grid.Row>
          {issue.properties.labels.map((label) => (
            <Grid.Column>
              <Menu fluid vertical>
                <Menu.Item style={{ textAlign: "center" }} className="header">
                  {label}
                </Menu.Item>
              </Menu>
            </Grid.Column>
          ))}
        </Grid.Row>
      </Grid>
      <button
        className="ui primary button"
        onClick={handleEditLabelClick}
        style={{
          visibility:
            activeUserSession.userID == issue.properties.user_id
              ? "visible"
              : "hidden",
        }}
      >
        Edit Labels
      </button>
      <h2>State</h2>
      <Grid columns={8}>
        <Grid.Row>
          <Grid.Column>
            <Menu fluid vertical>
              <Menu.Item style={{ textAlign: "center" }} className="header">
                {issue.properties.state_name}
              </Menu.Item>
            </Menu>
          </Grid.Column>
        </Grid.Row>
      </Grid>
      <button
        className="ui primary button"
        onClick={handleEditStateClick}
        style={{
          visibility:
            activeUserSession.userID == issue.properties.user_id
              ? "visible"
              : "hidden",
        }}
      >
        Change state
      </button>
      <h2>Comments:</h2>
    </div>
  );
}
