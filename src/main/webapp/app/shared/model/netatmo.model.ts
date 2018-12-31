export interface INetatmo {
  id?: number;
  clientId?: string;
  clientSecret?: string;
  email?: string;
  password?: string;
  enabled?: boolean;
}

export const defaultValue: Readonly<INetatmo> = {
  enabled: false
};
