import React from "react";
import { Dropdown, Grid, Menu } from "semantic-ui-react";
import { User } from "../common/types/user";

/// UserDisplay component props
type UserDisplayProps = {
  handleEditUserClick(): void;
  handleDeleteUser(): void;
  handleCreateProject(): void;
  userDetails: User;
};

export function UserDisplay({
  handleEditUserClick,
  handleDeleteUser,
  handleCreateProject,
  userDetails,
}: UserDisplayProps) {
  return (
    <div>
      <Menu vertical color="blue" inverted>
        <Dropdown item text="Actions" color="blue">
          <Dropdown.Menu>
            <Dropdown.Item onClick={handleEditUserClick}>
              Edit User
            </Dropdown.Item>
            <Dropdown.Item onClick={handleDeleteUser}>
              Delete User
            </Dropdown.Item>
            <Dropdown.Item onClick={handleCreateProject}>
              Create Project
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
                {userDetails.properties.username}
              </Menu.Item>
            </Menu>
          </Grid.Column>
        </Grid.Row>
      </Grid>
      <h2>Projects:</h2>
    </div>
  );
}
