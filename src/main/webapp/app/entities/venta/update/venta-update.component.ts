import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { VentaFormService, VentaFormGroup } from './venta-form.service';
import { IVenta } from '../venta.model';
import { VentaService } from '../service/venta.service';
import { IReporte } from 'app/entities/reporte/reporte.model';
import { ReporteService } from 'app/entities/reporte/service/reporte.service';

@Component({
  selector: 'jhi-venta-update',
  templateUrl: './venta-update.component.html',
})
export class VentaUpdateComponent implements OnInit {
  isSaving = false;
  venta: IVenta | null = null;

  reportesSharedCollection: IReporte[] = [];

  editForm: VentaFormGroup = this.ventaFormService.createVentaFormGroup();

  constructor(
    protected ventaService: VentaService,
    protected ventaFormService: VentaFormService,
    protected reporteService: ReporteService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareReporte = (o1: IReporte | null, o2: IReporte | null): boolean => this.reporteService.compareReporte(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venta }) => {
      this.venta = venta;
      if (venta) {
        this.updateForm(venta);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const venta = this.ventaFormService.getVenta(this.editForm);
    if (venta.id !== null) {
      this.subscribeToSaveResponse(this.ventaService.update(venta));
    } else {
      this.subscribeToSaveResponse(this.ventaService.create(venta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVenta>>): void {
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

  protected updateForm(venta: IVenta): void {
    this.venta = venta;
    this.ventaFormService.resetForm(this.editForm, venta);

    this.reportesSharedCollection = this.reporteService.addReporteToCollectionIfMissing<IReporte>(
      this.reportesSharedCollection,
      venta.reporte
    );
  }

  protected loadRelationshipsOptions(): void {
    this.reporteService
      .query()
      .pipe(map((res: HttpResponse<IReporte[]>) => res.body ?? []))
      .pipe(map((reportes: IReporte[]) => this.reporteService.addReporteToCollectionIfMissing<IReporte>(reportes, this.venta?.reporte)))
      .subscribe((reportes: IReporte[]) => (this.reportesSharedCollection = reportes));
  }
}
