import dayjs from 'dayjs/esm';
import { IReporte } from 'app/entities/reporte/reporte.model';

export interface IVenta {
  id: number;
  fecha?: dayjs.Dayjs | null;
  precio?: number | null;
  foreignId?: number | null;
  reporte?: Pick<IReporte, 'id'> | null;
}

export type NewVenta = Omit<IVenta, 'id'> & { id: null };
