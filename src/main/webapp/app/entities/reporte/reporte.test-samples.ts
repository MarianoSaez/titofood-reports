import dayjs from 'dayjs/esm';

import { TipoReporte } from 'app/entities/enumerations/tipo-reporte.model';

import { IReporte, NewReporte } from './reporte.model';

export const sampleWithRequiredData: IReporte = {
  id: 83587,
};

export const sampleWithPartialData: IReporte = {
  id: 30284,
  tipo: TipoReporte['CANCELAR'],
  fechaInicio: dayjs('2023-01-23T05:47'),
  intervalo: 'compelling',
  cancelado: true,
};

export const sampleWithFullData: IReporte = {
  id: 99136,
  tipo: TipoReporte['RECURR'],
  fechaInicio: dayjs('2023-01-23T06:59'),
  fechaFin: dayjs('2023-01-23T03:27'),
  intervalo: 'bluetooth',
  foreignId: 2680,
  cancelado: false,
};

export const sampleWithNewData: NewReporte = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
