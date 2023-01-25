import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ReporteFormService, ReporteFormGroup } from './reporte-form.service';
import { IReporte } from '../reporte.model';
import { ReporteService } from '../service/reporte.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';
import { TipoReporte } from 'app/entities/enumerations/tipo-reporte.model';

@Component({
  selector: 'jhi-reporte-update',
  templateUrl: './reporte-update.component.html',
})
export class ReporteUpdateComponent implements OnInit {
  isSaving = false;
  reporte: IReporte | null = null;
  tipoReporteValues = Object.keys(TipoReporte);

  ventasSharedCollection: IVenta[] = [];

  editForm: ReporteFormGroup = this.reporteFormService.createReporteFormGroup();

  constructor(
    protected reporteService: ReporteService,
    protected reporteFormService: ReporteFormService,
    protected ventaService: VentaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareVenta = (o1: IVenta | null, o2: IVenta | null): boolean => this.ventaService.compareVenta(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reporte }) => {
      this.reporte = reporte;
      if (reporte) {
        this.updateForm(reporte);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reporte = this.reporteFormService.getReporte(this.editForm);
    if (reporte.id !== null) {
      this.subscribeToSaveResponse(this.reporteService.update(reporte));
    } else {
      this.subscribeToSaveResponse(this.reporteService.create(reporte));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReporte>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(reporte: IReporte): void {
    this.reporte = reporte;
    this.reporteFormService.resetForm(this.editForm, reporte);

    this.ventasSharedCollection = this.ventaService.addVentaToCollectionIfMissing<IVenta>(
      this.ventasSharedCollection,
      ...(reporte.ventas ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing<IVenta>(ventas, ...(this.reporte?.ventas ?? []))))
      .subscribe((ventas: IVenta[]) => (this.ventasSharedCollection = ventas));
  }
}
