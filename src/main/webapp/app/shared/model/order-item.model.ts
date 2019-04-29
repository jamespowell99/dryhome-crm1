import { IProduct } from 'app/shared/model/product.model';

export interface IOrderItem {
  id?: number;
  price?: number;
  quantity?: number;
  notes?: string;
  serialNumber?: string;
  product?: IProduct;
}

export const defaultValue: Readonly<IOrderItem> = {};
