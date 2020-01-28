export interface IType {
    id?: number;
}

export class Type implements IType {
    constructor(public id?: number) {}
}
