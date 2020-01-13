import { IProduct } from 'app/shared/model/product.model';
import { IManualInvoice } from 'app/shared/model/manual-invoice.model';

export interface IManualInvoiceItem {
  id?: number;
  quantity?: number;
  price?: number;
  product?: IProduct;
  manualInvoice?: IManualInvoice;
}

export const defaultValue: Readonly<IManualInvoiceItem> = {};
