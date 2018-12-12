import { Moment } from 'moment';

export interface ITemperature {
  id?: number;
  value?: number;
  timestamp?: Moment;
}

export const defaultValue: Readonly<ITemperature> = {};
