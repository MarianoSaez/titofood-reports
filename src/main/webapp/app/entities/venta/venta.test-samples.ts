import dayjs from 'dayjs/esm';

import { IVenta, NewVenta } from './venta.model';

export const sampleWithRequiredData: IVenta = {
  id: 20448,
};

export const sampleWithPartialData: IVenta = {
  id: 11474,
  precio: 4145,
};

export const sampleWithFullData: IVenta = {
  id: 56225,
  fecha: dayjs('2023-01-23T03:31'),
  precio: 57127,
  foreignId: 41728,
};

export const sampleWithNewData: NewVenta = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
