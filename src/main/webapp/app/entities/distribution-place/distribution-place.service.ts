import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDistributionPlace } from 'app/shared/model/distribution-place.model';

type EntityResponseType = HttpResponse<IDistributionPlace>;
type EntityArrayResponseType = HttpResponse<IDistributionPlace[]>;

@Injectable({ providedIn: 'root' })
export class DistributionPlaceService {
    public resourceUrl = SERVER_API_URL + 'api/distribution-places';

    constructor(protected http: HttpClient) {}

    create(distributionPlace: IDistributionPlace): Observable<EntityResponseType> {
        return this.http.post<IDistributionPlace>(this.resourceUrl, distributionPlace, { observe: 'response' });
    }

    update(distributionPlace: IDistributionPlace): Observable<EntityResponseType> {
        return this.http.put<IDistributionPlace>(this.resourceUrl, distributionPlace, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDistributionPlace>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDistributionPlace[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
