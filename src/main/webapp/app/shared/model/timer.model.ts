import { Moment } from 'moment';
import { IController } from 'app/shared/model//controller.model';

export const enum Repeat {
  DAY = 'DAY',
  WEEK = 'WEEK',
  MONTH = 'MONTH',
  YEAR = 'YEAR'
}

export interface ITimer {
  id?: number;
  name?: string;
  start?: Moment;
  stop?: Moment;
  repeat?: Repeat;
  controllers?: IController[];
}

export const defaultValue: Readonly<ITimer> = {};
