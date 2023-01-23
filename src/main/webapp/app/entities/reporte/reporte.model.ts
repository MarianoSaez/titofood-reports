import dayjs from 'dayjs/esm';
import { TipoReporte } from 'app/entities/enumerations/tipo-reporte.model';

export interface IReporte {
  id: number;
  tipo?: TipoReporte | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  intervalo?: string | null;
}

export type NewReporte = Omit<IReporte, 'id'> & { id: null };
