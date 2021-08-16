import { ActionsSiren } from "./actions";
import { LinksSiren } from "./links";
import { UserProperty } from "./user";

export type ProjectEntity = {
  class: Array<string>;
  rel: Array<string>;
  properties: UserProperty;
  links: LinksSiren;
};

export type ProjectProperty = {
  name: string;
  description: string;
  project_id: number;
  labels: Array<String>;
  states: Array<String>;
  transitions: Array<Transition>;
};

export type Transition = {
  project_id: number;
  state_name: string;
  transits_to: string;
};

export type ProjectType = {
  class: string;
  properties: ProjectProperty;
  entities: Array<ProjectEntity>;
  actions: Array<ActionsSiren>;
  links: Array<LinksSiren>;
};
