import React from "react";
import { Menu, Segment } from "semantic-ui-react";
import { Link } from "react-router-dom";
import { UserSession } from "../common/types/user";

/// Navbar component props
type navbarProps = {
  activeItem: string;
  user: UserSession;
};

export function Navbar ( {activeItem, user} : navbarProps) {

  return (

    <Segment inverted>
      <Menu inverted pointing secondary>

        {/* Home page */}
        <Menu.Item name="home" active={activeItem === "home"}>
          <Link to="/">Home</Link>
        </Menu.Item>

        {/* All projects page */}
        <Menu.Item name="All Projects" active={activeItem === "projects"}>
          <Link to="/projects">Projects</Link>
        </Menu.Item>

        {user.isLoggedIn ?

          <div className="right menu">

            {/* User page */}
            <Menu.Item name="user" active={activeItem === "user"}>
              <Link to="/user">{user.username}</Link>
            </Menu.Item>
            
            {/* Logout option */}
            <Menu.Item name="logout" active={activeItem === "logout"}>
              <Link to="/logout">Logout</Link>
            </Menu.Item>
            
          </div>

          :
          
          <div className="right menu">
            {/* Login or Register page */}
            <Menu.Item name="Login or Register" active={activeItem === "Login or Register"}>
              <Link to="/loginRegister">Login or Register</Link>
            </Menu.Item>
          </div>

        }

      </Menu>
    </Segment>
        
  )
}
