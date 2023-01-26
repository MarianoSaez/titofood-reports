import dayjs from 'dayjs/esm';

import { TipoReporte } from 'app/entities/enumerations/tipo-reporte.model';

import { IReporte, NewReporte } from './reporte.model';

export const sampleWithRequiredData: IReporte = {
  id: 83587,
};

export const sampleWithPartialData: IReporte = {
  id: 83232,
  tipo: TipoReporte['RECURR'],
  fechaInicio: dayjs('2023-01-22T21:35'),
  intervalo: 'Pants Orchestrator',
};

export const sampleWithFullData: IReporte = {
  id: 23334,
  tipo: TipoReporte['HIST'],
  fechaInicio: dayjs('2023-01-23T03:27'),
  fechaFin: dayjs('2023-01-23T14:19'),
  intervalo: 'Savings Ergonomic Creative',
  foreignId: 55159,
};

export const sampleWithNewData: NewReporte = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
