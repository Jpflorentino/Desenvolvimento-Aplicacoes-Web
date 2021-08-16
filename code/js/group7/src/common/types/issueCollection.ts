import { ActionsSiren } from "./actions";
import { LinksSiren } from "./links";

export type IssueCollectionEntityProperties = {
  user_id: number;
  project_id: number;
  issue_id: number;
  name: string;
  description: string;
  opened_on: number;
  closed_on: number;
  state_name: string;
};

export type IssueCollectionSubEntityProperties = {
  user_id: number;
  username: string;
};

export type IssueCollectionSubEntity = {
  class: Array<string>;
  rel: Array<string>;
  properties: IssueCollectionSubEntityProperties;
  links: Array<LinksSiren>;
};

export type IssueCollectionEntity = {
  class: Array<string>;
  rel: Array<string>;
  entities: Array<IssueCollectionSubEntity>;
  properties: IssueCollectionEntityProperties;
  links: Array<LinksSiren>;
};

export type IssueCollectionProperties = {
  collectionSize: number;
  totalPages: number;
  pageIndex: number;
  pageSize: number;
  project_id: number;
};

export type IssueCollection = {
  class: Array<string>;
  properties: IssueCollectionProperties;
  entities: Array<IssueCollectionEntity>;
  actions: Array<ActionsSiren>;
  links: Array<LinksSiren>;
};
