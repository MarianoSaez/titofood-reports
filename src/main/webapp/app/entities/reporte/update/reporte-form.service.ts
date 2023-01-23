import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReporte, NewReporte } from '../reporte.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReporte for edit and NewReporteFormGroupInput for create.
 */
type ReporteFormGroupInput = IReporte | PartialWithRequiredKeyOf<NewReporte>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReporte | NewReporte> = Omit<T, 'fechaInicio' | 'fechaFin'> & {
  fechaInicio?: string | null;
  fechaFin?: string | null;
};

type ReporteFormRawValue = FormValueOf<IReporte>;

type NewReporteFormRawValue = FormValueOf<NewReporte>;

type ReporteFormDefaults = Pick<NewReporte, 'id' | 'fechaInicio' | 'fechaFin'>;

type ReporteFormGroupContent = {
  id: FormControl<ReporteFormRawValue['id'] | NewReporte['id']>;
  tipo: FormControl<ReporteFormRawValue['tipo']>;
  fechaInicio: FormControl<ReporteFormRawValue['fechaInicio']>;
  fechaFin: FormControl<ReporteFormRawValue['fechaFin']>;
  intervalo: FormControl<ReporteFormRawValue['intervalo']>;
};

export type ReporteFormGroup = FormGroup<ReporteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReporteFormService {
  createReporteFormGroup(reporte: ReporteFormGroupInput = { id: null }): ReporteFormGroup {
    const reporteRawValue = this.convertReporteToReporteRawValue({
      ...this.getFormDefaults(),
      ...reporte,
    });
    return new FormGroup<ReporteFormGroupContent>({
      id: new FormControl(
        { value: reporteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      tipo: new FormControl(reporteRawValue.tipo),
      fechaInicio: new FormControl(reporteRawValue.fechaInicio),
      fechaFin: new FormControl(reporteRawValue.fechaFin),
      intervalo: new FormControl(reporteRawValue.intervalo),
    });
  }

  getReporte(form: ReporteFormGroup): IReporte | NewReporte {
    return this.convertReporteRawValueToReporte(form.getRawValue() as ReporteFormRawValue | NewReporteFormRawValue);
  }

  resetForm(form: ReporteFormGroup, reporte: ReporteFormGroupInput): void {
    const reporteRawValue = this.convertReporteToReporteRawValue({ ...this.getFormDefaults(), ...reporte });
    form.reset(
      {
        ...reporteRawValue,
        id: { value: reporteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ReporteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaInicio: currentTime,
      fechaFin: currentTime,
    };
  }

  private convertReporteRawValueToReporte(rawReporte: ReporteFormRawValue | NewReporteFormRawValue): IReporte | NewReporte {
    return {
      ...rawReporte,
      fechaInicio: dayjs(rawReporte.fechaInicio, DATE_TIME_FORMAT),
      fechaFin: dayjs(rawReporte.fechaFin, DATE_TIME_FORMAT),
    };
  }

  private convertReporteToReporteRawValue(
    reporte: IReporte | (Partial<NewReporte> & ReporteFormDefaults)
  ): ReporteFormRawValue | PartialWithRequiredKeyOf<NewReporteFormRawValue> {
    return {
      ...reporte,
      fechaInicio: reporte.fechaInicio ? reporte.fechaInicio.format(DATE_TIME_FORMAT) : undefined,
      fechaFin: reporte.fechaFin ? reporte.fechaFin.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
