import { ActionsSiren } from "./actions";
import { LinksSiren } from "./links";

export type IssueEntityProperties = {
  user_id: number;
  username: string;
};
export type IssueEntity = {
  class: Array<string>;
  rel: Array<string>;
  properties: IssueEntityProperties;
  links: Array<LinksSiren>;
};
export type IssueProperties = {
  user_id: number;
  project_id: number;
  issue_id: number;
  name: string;
  description: string;
  labels: Array<string>;
  opened_on: number;
  closed_on: number;
  state_name: string;
};
export type IssueType = {
  class: Array<string>;
  properties: IssueProperties;
  entities: Array<IssueEntity>;
  actions: Array<ActionsSiren>;
  links: Array<LinksSiren>;
};
