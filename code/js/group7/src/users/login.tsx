import React, { useEffect, useState, useContext } from "react";
import { useHistory } from "react-router-dom";
import { UserSessionContext } from "../users/userSessionContext";

import base64 from "base-64";

/// Login component props
type LoginProps = {
  getUserURI: string;
  handleLogInForm: (login: boolean) => void;
};

export function Login({ getUserURI, handleLogInForm }: LoginProps) {
  /// Alternative: (props) : LoginProps

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [submit, setSubmit] = useState(false);

  const history = useHistory();

  const { updateUserSession } = useContext(UserSessionContext);

  useEffect(() => {
    if (!submit) return;

    setSubmit(false);

    const body = {
      username: username,
      password: password,
    };

    const options = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    };

    fetch(getUserURI, options)
      .then((resp) => {
        return resp.json();
      })
      .then((json) => {
        if (json.status == null) {
          let userid = json.properties.user_id;

          const creds = base64.encode(body.username + ":" + body.password);

          /// Update user session
          updateUserSession({
            /// Already inside a useEffect
            isLoggedIn: true,
            username: body.username,
            userID: userid,
            authCredentials: creds,
          });
          history.push("/");
        } else {
          alert(json.detail);
        }
      })
      .catch((err) => alert("Erro: " + err));
  }, [submit]);

  function handleUserCreationSubmit() {
    setSubmit(true);
  }

  return (
    <div
      className="ui middle aligned center aligned grid"
      style={{ marginTop: 125 }}
    >
      <div className="column" style={{ maxWidth: 380 }}>
        <h2 className="ui header centered">
          <div className="content">Group 7 Application</div>
        </h2>

        <form className={`ui large form`}>
          <div className="ui segment">
            {/* Username */}
            <div className="required field">
              {" "}
              <div className="ui input left icon">
                <i className="user icon"></i>
                <input
                  type="text"
                  name="username"
                  placeholder="Your username"
                  onChange={(event) => setUsername(event.target.value)}
                />{" "}
              </div>
            </div>

            {/* Password */}
            <div className="required field">
              {" "}
              <div className="ui input left icon">
                <i className="key icon"></i>
                <input
                  type="password"
                  name="password"
                  placeholder="Your password"
                  onChange={(event) => setPassword(event.target.value)}
                />{" "}
              </div>
            </div>

            {/* Submmit */}
            <button
              className="ui fluid large submit button"
              type="button"
              onClick={handleUserCreationSubmit}
            >
              <i className="sign in icon"></i>Sign in
            </button>

            <a onClick={() => handleLogInForm(false)}>
              Don't have an account? Register here
            </a>
          </div>

          <div className="ui error message">
            {" "}
            <p>Enter a non empty username and password</p>
          </div>
        </form>
      </div>
    </div>
  );
}
