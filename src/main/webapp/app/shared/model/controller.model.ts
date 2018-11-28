export const enum IO {
  INPUT = 'INPUT',
  OUTPUT = 'OUTPUT'
}

export interface IController {
  id?: number;
  name?: string;
  mode?: IO;
  pinName?: string;
  pinId?: number;
  status?: boolean;
}

export const defaultValue: Readonly<IController> = {};
