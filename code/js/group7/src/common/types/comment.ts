import { UserProperty } from "./user";
import { ActionsSiren } from "./actions";
import { LinksSiren } from "./links";

export type CommentEntity = {
  class: Array<string>;
  rel: Array<string>;
  properties: UserProperty;
  links: Array<LinksSiren>;
};

export type CommentProperties = {
  user_id: number;
  issue_id: number;
  comment_id: number;
  comment_text: string;
  creation_date: number;
};

export type CommentType = {
  class: Array<string>;
  properties: CommentProperties;
  entities: Array<CommentEntity>;
  actions: Array<ActionsSiren>;
  links: Array<LinksSiren>;
};
