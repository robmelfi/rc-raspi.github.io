export interface ISensor {
  id?: number;
  name?: string;
  description?: string;
  imageContentType?: string;
  image?: any;
}

export const defaultValue: Readonly<ISensor> = {};
