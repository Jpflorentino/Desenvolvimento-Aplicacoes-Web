import React, { useContext, useEffect, useState } from "react";
import { useFetch } from "../common/hooks/useFetch";
import { ProjectCollectionEntity, ProjectCollection } from "../common/types/projectCollection";
import { useHistory } from "react-router-dom";
import { Loader } from "../common/utils/loader";
import { Card } from "semantic-ui-react";
import { PageButtons } from "../common/utils/pageButtons";
import { NavbarContext } from "../navbar/navbarContext";
import { UserSessionContext} from "../users/userSessionContext";

/// Project collection component props
type ProjectCollectionProps = {
  uri: string;
};

export function ProjectCollection({ uri } : ProjectCollectionProps) {

  /// useState
  const [pageState, setPageState] = useState({ currentPage: 1, pageSize: 9 });
  
  /// useHistory
  const history = useHistory();

  /// useContext
  const { activeUserSession } = useContext(UserSessionContext);
  const { activeItem, updateActiveItem } = useContext(NavbarContext);

  /// Update navbar active item
  useEffect(() => { /// Avoid component side-effects
    if(activeItem !== "projects") updateActiveItem("projects");
  }, []);

  function handleProjectClick(project: ProjectCollectionEntity) {
    if (activeUserSession.isLoggedIn)
      history.push(project.links[0].href.replace("/group7api", ""));
    else history.push("/loginRegister");
  }

  const fetchState = useFetch(
    uri
      .replace("{index}", pageState.currentPage.toString())
      .replace("{pageSize}", pageState.pageSize.toString()),
    "GET",
    undefined,
    undefined
  );

  const body = fetchState.type == "response" ? fetchState.body : "";

  switch (fetchState.type) {
    case "error": {
      return (
        <div>
          <p>{fetchState.type}</p>
          <p>{fetchState.error}</p>
        </div>
      );
    }

    case "fetching": {
      return <Loader />;
    }

    case "started": {
      return <Loader />;
    }

    case "response": {
      const projectCollection: ProjectCollection = JSON.parse(body);

      const parsedProjects = projectCollection.entities;

      return (
        <div>
          <div style={styles.divPadding}>
            
            <h2 style={styles.title}>All Projects</h2>

            <Card.Group itemsPerRow={3}>
              {parsedProjects.map((project: ProjectCollectionEntity) => (
                <Card
                  key={project.properties.project_id}
                  onClick={() => handleProjectClick(project)}
                >
                  <Card.Content>
                    <Card.Header>{project.properties.name}</Card.Header>
                    <Card.Meta>
                      Project created by {' '} {project.entities[0].properties.username}
                    </Card.Meta>
                    <Card.Description>
                      {project.properties.description}
                    </Card.Description>
                  </Card.Content>
                </Card>
              ))}
            </Card.Group>

          </div>

          <PageButtons
            pageState={pageState}
            setPageState={setPageState}
            collection={projectCollection}
          />
        </div>
      );
    }
  }
}

// Styles
const styles = {
  divPadding: {
    paddingRight: "80px",
    paddingLeft: "80px",
    paddingTop: "20px",
  },
  title: {
    textAlign: "center" as "center",
    paddingBottom: "20px",
  },
};
