import dayjs from 'dayjs/esm';
import { IVenta } from 'app/entities/venta/venta.model';
import { TipoReporte } from 'app/entities/enumerations/tipo-reporte.model';

export interface IReporte {
  id: number;
  tipo?: TipoReporte | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  intervalo?: string | null;
  ventas?: Pick<IVenta, 'id'>[] | null;
}

export type NewReporte = Omit<IReporte, 'id'> & { id: null };
