import dayjs from 'dayjs/esm';

import { TipoReporte } from 'app/entities/enumerations/tipo-reporte.model';

import { IReporte, NewReporte } from './reporte.model';

export const sampleWithRequiredData: IReporte = {
  id: 83587,
};

export const sampleWithPartialData: IReporte = {
  id: 2695,
  tipo: TipoReporte['HIST'],
  fechaInicio: dayjs('2023-01-23T13:32'),
  intervalo: 'Savings Metrics Manager',
};

export const sampleWithFullData: IReporte = {
  id: 72257,
  tipo: TipoReporte['RECURR'],
  fechaInicio: dayjs('2023-01-23T01:23'),
  fechaFin: dayjs('2023-01-23T05:43'),
  intervalo: 'Movies',
};

export const sampleWithNewData: NewReporte = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
