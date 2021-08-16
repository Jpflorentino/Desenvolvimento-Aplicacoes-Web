import { LinksSiren } from "./links";

export type ProjectCollectionEntityProperties = {
  user_id: number;
  project_id: number;
  name: string;
  description: string;
};

export type ProjectCollectionSubEntityProperties = {
  user_id: number;
  username: string;
};

export type ProjectCollectionSubEntity = {
  class: Array<string>;
  rel: Array<string>;
  properties: ProjectCollectionSubEntityProperties;
  links: Array<LinksSiren>;
};

export type ProjectCollectionEntity = {
  class: Array<string>;
  entities: Array<ProjectCollectionSubEntity> | null; //user projects list doesn't have sub entities, so it's either null or with an owner entity
  rel: Array<string>;
  properties: ProjectCollectionEntityProperties;
  links: Array<LinksSiren>;
};

export type ProjectCollectionProperty = {
  collectionSize: number;
  totalPages: number;
  pageIndex: number;
  pageSize: number;
};

export type ProjectCollection = {
  class: Array<string>;
  properties: ProjectCollectionProperty;
  entities: Array<ProjectCollectionEntity>;
  links: Array<LinksSiren>;
};
