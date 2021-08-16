import { FieldsSiren } from "./fields";

export type ActionsSiren = {
  name: string;
  href: string;
  title: string;
  method: string;
  type: string;
  fields: Array<FieldsSiren>;
};
