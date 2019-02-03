export interface IComment {
    id?: number;
    text?: string;
    distributionId?: number;
    userId?: number;
}

export class Comment implements IComment {
    constructor(public id?: number, public text?: string, public distributionId?: number, public userId?: number) {}
}
