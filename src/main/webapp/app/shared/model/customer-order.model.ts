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
  //  todo not sure if this should go here
  customerName?: string;
  orderSubTotal?: number;
  vatAmount?: number;
  orderTotal?: number;
  invoiceContact?: string;
  deliveryContact?: string;
  invoiceAddress?: IAddress;
  deliveryAddress?: IAddress;
}
// todo move vatRate to constant
export const defaultValue: Readonly<ICustomerOrder> = { items: [{}], vatRate: 20 };
