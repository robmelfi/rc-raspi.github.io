import { Moment } from 'moment';

export interface IHumidity {
  id?: number;
  value?: number;
  timestamp?: Moment;
}

export const defaultValue: Readonly<IHumidity> = {};
