import { Moment } from 'moment';
import { IComment } from 'app/shared/model/comment.model';
import { IUser } from 'app/core/user/user.model';

export const enum Type {
    DISTRIBUTION = 'DISTRIBUTION',
    WORKSHOP = 'WORKSHOP'
}

export interface IDistribution {
    id?: number;
    date?: Moment;
    text?: any;
    endDate?: Moment;
    startDate?: Moment;
    minUsers?: number;
    type?: Type;
    comments?: IComment[];
    placeName?: string;
    placeId?: number;
    users?: IUser[];
}

export class Distribution implements IDistribution {
    constructor(
        public id?: number,
        public date?: Moment,
        public text?: any,
        public endDate?: Moment,
        public startDate?: Moment,
        public minUsers?: number,
        public type?: Type,
        public comments?: IComment[],
        public placeName?: string,
        public placeId?: number,
        public users?: IUser[]
    ) {}
}
