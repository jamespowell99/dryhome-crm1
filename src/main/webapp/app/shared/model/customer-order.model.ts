import { Moment } from 'moment';
import { IOrderItem } from 'app/shared/model/order-item.model';

export const enum OrderMethod {
  PHONE = 'PHONE',
  FAX = 'FAX',
  EMAIL = 'EMAIL',
  IN_PERSON = 'IN_PERSON'
}

export interface IAddress {
  address1?: string;
  address2?: string;
  address3?: string;
  town?: string;
  postCode?: string;
}

export interface ICustomerOrderReport {
  count?: number;
  subTotal?: number;
  vatAmount?: number;
  total?: number;
  orders?: ICustomerOrder[];
}

export interface ICustomerOrder {
  id?: number;
  orderNumber?: string;
  orderDate?: Moment;
  notes1?: string;
  notes2?: string;
  despatchDate?: Moment;
  invoiceDate?: Moment;
  paymentDate?: Moment;
  vatRate?: number;
  internalNotes?: string;
  invoiceNumber?: string;
  paymentStatus?: string;
  paymentType?: string;
  paymentAmount?: number;
  placedBy?: string;
  method?: OrderMethod;
  items?: IOrderItem[];
  customerId?: number;
  customerName?: string;
  customerType?: string;
  subTotal?: number;
  vatAmount?: number;
  total?: number;
  invoiceContact?: string;
  deliveryContact?: string;
  invoiceAddress?: IAddress;
  deliveryAddress?: IAddress;
}

export interface ICustomerOrderStats {
  month?: IStat;
  year?: IStat;
  past12Months?: IStat;
}

export interface IStat {
  current?: IStatIndividual;
  last?: IStatIndividual;
  diff?: number;
}

export interface IStatIndividual {
  count?: number;
  total?: number;
  start?: Moment;
  end?: Moment;
}
// todo move vatRate to constant
export const defaultValue: Readonly<ICustomerOrder> = { items: [{}], vatRate: 20 };
