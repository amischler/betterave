export interface IDistributionPlace {
    id?: number;
    name?: string;
}

export class DistributionPlace implements IDistributionPlace {
    constructor(public id?: number, public name?: string) {}
}
