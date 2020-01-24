import { Pipe, PipeTransform } from '@angular/core';
import { Injectable } from '@angular/core';

import { Account } from 'app/core';

@Injectable()
@Pipe({ name: 'accountFormat' })
export class AccountFormatPipe implements PipeTransform {
    transform(account: Account) {
        if (account.firstName && account.firstName.length > 0 && (account.lastName || account.lastName.length > 0)) {
            return account.firstName + ' ' + account.lastName;
        } else if (account.firstName && account.firstName.length > 0) {
            return account.firstName;
        } else {
            return account.login;
        }
    }
}
