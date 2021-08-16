import React from "react";
import { Card } from "semantic-ui-react";
import { CommentCollectionEntity } from "../common/types/commentCollection";
import { UserSession } from "../common/types/user";

export function CommentsList({
  commentsList,
  handleCommentClick,
}: {
  commentsList: Array<CommentCollectionEntity>;
  handleCommentClick(comment_id: string): void;
}) {
  return (
    <Card.Group itemsPerRow={2}>
      {commentsList.map((comment: CommentCollectionEntity) => (
        <Card
          key={comment.properties.comment_id}
          onClick={() =>
            handleCommentClick(comment.properties.comment_id.toString())
          }
        >
          <Card.Content>
            <Card.Header>{comment.properties.comment_text}</Card.Header>
            <Card.Meta>
              Comment created by {' '} {comment.entities[0].properties.username}
            </Card.Meta>
          </Card.Content>
        </Card>
      ))}
    </Card.Group>
  );
}
