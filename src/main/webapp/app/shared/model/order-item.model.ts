import { IProduct } from 'app/shared/model/product.model';
import { ICustomerOrder } from 'app/shared/model/customer-order.model';

export interface IOrderItem {
  id?: number;
  price?: number;
  quantity?: number;
  notes?: string;
  serialNumber?: string;
  product?: string;
}

export const defaultValue: Readonly<IOrderItem> = {};
