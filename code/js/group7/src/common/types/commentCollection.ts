import { ActionsSiren } from "./actions";
import { LinksSiren } from "./links";

export type CommentCollectionSubEntityProperties = {
  user_id: number;
  username: string;
};

export type CommentCollectionSubEntity = {
  class: Array<string>;
  rel: Array<string>;
  properties: CommentCollectionSubEntityProperties;
  links: Array<LinksSiren>;
};

export type CommentCollectionEntityProperties = {
  user_id: number;
  issue_id: number;
  comment_id: number;
  comment_text: string;
  creation_date: number;
};

export type CommentCollectionEntity = {
  class: Array<string>;
  rel: Array<string>;
  entities: Array<CommentCollectionSubEntity>;
  properties: CommentCollectionEntityProperties;
  links: Array<LinksSiren>;
};
export type CommentCollectionProperties = {
  collectionSize: number;
  totalPages: number;
  pageIndex: number;
  pageSize: number;
  issue_id: number;
};

export type CommentCollection = {
  class: Array<string>;
  properties: CommentCollectionProperties;
  entities: Array<CommentCollectionEntity>;
  actions: Array<ActionsSiren>;
  links: Array<LinksSiren>;
};
