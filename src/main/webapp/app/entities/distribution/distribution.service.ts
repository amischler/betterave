import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDistribution } from 'app/shared/model/distribution.model';

type EntityResponseType = HttpResponse<IDistribution>;
type EntityArrayResponseType = HttpResponse<IDistribution[]>;

@Injectable({ providedIn: 'root' })
export class DistributionService {
    public resourceUrl = SERVER_API_URL + 'api/distributions';

    constructor(protected http: HttpClient) {}

    create(distribution: IDistribution): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(distribution);
        return this.http
            .post<IDistribution>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(distribution: IDistribution): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(distribution);
        return this.http
            .put<IDistribution>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDistribution>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDistribution[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(distribution: IDistribution): IDistribution {
        const copy: IDistribution = Object.assign({}, distribution, {
            date: distribution.date != null && distribution.date.isValid() ? distribution.date.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.date = res.body.date != null ? moment(res.body.date) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((distribution: IDistribution) => {
                distribution.date = distribution.date != null ? moment(distribution.date) : null;
            });
        }
        return res;
    }

    subscribe(id: number): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/${id}/subscribe`, { observe: 'response' });
    }
}
