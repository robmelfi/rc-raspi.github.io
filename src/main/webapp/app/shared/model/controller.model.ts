export const enum IO {
  INPUT = 'INPUT',
  OUTPUT = 'OUTPUT'
}

export interface IController {
  id?: number;
  name?: string;
  mode?: IO;
  state?: boolean;
  netatmo?: boolean;
  pinName?: string;
  pinId?: number;
  sensorName?: string;
  sensorId?: number;
  timerName?: string;
  timerId?: number;
}

export const defaultValue: Readonly<IController> = {
  state: false,
  netatmo: false
};
