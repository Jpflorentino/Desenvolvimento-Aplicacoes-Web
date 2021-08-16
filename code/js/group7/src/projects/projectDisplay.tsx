import React, { useContext } from "react";
import { Transition } from "../common/types/project";
import { ProjectType } from "../common/types/project";
import { Dropdown, Grid, Menu } from "semantic-ui-react";
import { UserSessionContext} from "../users/userSessionContext";

/// ProjectDisplay component props
type ProjectDisplayProps = {
  project: ProjectType;
  handleDelete(): void;
  handleEditProjectClick(): void;
  handleCreateIssue(): void;
};

export function ProjectDisplay({
  project,
  handleDelete,
  handleEditProjectClick,
  handleCreateIssue,
}: ProjectDisplayProps) {

  const { activeUserSession } = useContext(UserSessionContext);

  return (
    <div>

      <Menu vertical color="blue" inverted>
        <Dropdown item text="Actions" color="blue">
          <Dropdown.Menu>

            <Dropdown.Item
              onClick={handleEditProjectClick}
              style={{
                display:
                activeUserSession.userID == project.entities[0].properties.user_id
                    ? "visible"
                    : "none",
              }}
            >
              Edit Project
            </Dropdown.Item>

            <Dropdown.Item
              onClick={handleDelete}
              style={{
                display:
                activeUserSession.userID == project.entities[0].properties.user_id
                    ? "visible"
                    : "none",
              }}
            >
              Delete Project
            </Dropdown.Item>

            <Dropdown.Item onClick={handleCreateIssue}>
              Create Issue
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
                {project.properties.name}
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
                {project.properties.description}
              </Menu.Item>
            </Menu>
          </Grid.Column>
        </Grid.Row>
      </Grid>

      <h2>Project ID</h2>
      <Grid columns={8}>
        <Grid.Row>
          <Grid.Column>
            <Menu fluid vertical>
              <Menu.Item style={{ textAlign: "center" }} className="header">
                {project.properties.project_id}
              </Menu.Item>
            </Menu>
          </Grid.Column>
        </Grid.Row>
      </Grid>

      <h2>Labels</h2>
      <Grid columns={15}>
        <Grid.Row>
          {project.properties.labels.map((label, i) => (
            <Grid.Column key={i}>
              <Menu fluid vertical>
                <Menu.Item style={{ textAlign: "relative" }} className="header">
                  {label}
                </Menu.Item>
              </Menu>
            </Grid.Column>
          ))}
        </Grid.Row>
      </Grid>

      <h2>Available States</h2>
      <Grid columns={4}>
        <Grid.Row>
          {project.properties.states.map((state, i) => (
            <Grid.Column key={i}>
              <Menu fluid vertical>
                <Menu.Item style={{ textAlign: "center" }} className="header">
                  {state}
                </Menu.Item>
              </Menu>
            </Grid.Column>
          ))}
        </Grid.Row>
      </Grid>

      <h2>Possible Transitions</h2>
      <Grid columns={5}>
        <Grid.Row>
          {project.properties.transitions.map((transition: Transition, i) => (
            <Grid.Column key={i}>
              <Menu fluid vertical>
                <Menu.Item
                  style={{ textAlign: "center" }}
                  className="header"
                >{`${transition.state_name}->${transition.transits_to}`}</Menu.Item>
              </Menu>
            </Grid.Column>
          ))}
        </Grid.Row>
      </Grid>

      <h2>Issues:</h2>

    </div>
  );
}
