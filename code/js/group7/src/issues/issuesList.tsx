import React from "react";
import { Card } from "semantic-ui-react";
import { IssueCollectionEntity } from "../common/types/issueCollection";

export function IssuesList({
  issuesList,
  handleIssueClick,
}: {
  issuesList: Array<IssueCollectionEntity>;
  handleIssueClick(issue: IssueCollectionEntity): void;
}) {
  return (
    <Card.Group itemsPerRow={2}>
      {issuesList.map((issue: IssueCollectionEntity) => (
        <Card
          key={issue.properties.issue_id}
          onClick={() => handleIssueClick(issue)}
        >
          <Card.Content>
            <Card.Header>{issue.properties.name}</Card.Header>
            <Card.Meta>
              Issue created by {' '} {issue.entities[0].properties.username}
            </Card.Meta>
            <Card.Description>{issue.properties.description}</Card.Description>
          </Card.Content>
        </Card>
      ))}
    </Card.Group>
  );
}
