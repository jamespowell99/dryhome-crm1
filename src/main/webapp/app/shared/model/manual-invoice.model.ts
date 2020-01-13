import { Moment } from 'moment';
import { IManualInvoiceItem } from 'app/shared/model/manual-invoice-item.model';

export interface IManualInvoice {
  id?: number;
  invoiceNumber?: string;
  orderNumber?: string;
  invoiceDate?: Moment;
  ref?: string;
  customer?: string;
  address1?: string;
  address2?: string;
  address3?: string;
  town?: string;
  postCode?: string;
  telNo?: string;
  deliveryAddress1?: string;
  deliveryAddress2?: string;
  deliveryAddress3?: string;
  deliveryAddress4?: string;
  specialInstructions1?: string;
  specialInstructions2?: string;
  paymentDate?: Moment;
  paymentStatus?: string;
  paymentType?: string;
  paymentAmount?: number;
  vatRate?: number;
  items?: IManualInvoiceItem[];
  orderSubTotal?: number;
  vatAmount?: number;
  orderTotal?: number;
}

// todo move vatRate to constant
export const defaultValue: Readonly<IManualInvoice> = { items: [{}], vatRate: 20 };
