export interface ISensor {
  id?: number;
  name?: string;
  description?: string;
  imagePath?: string;
}

export const defaultValue: Readonly<ISensor> = {};
