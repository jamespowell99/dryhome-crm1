import { AxiosPromise } from 'axios';

export interface IPayload<T> {
  type: string;
  payload: AxiosPromise<T>;
  meta?: any;
}
export type ICrudSearchCustomerOrderAction<T> = (
  searchStatus: string,
  searchOrderNumber: string,
  searchInvoiceNumber: string,
  searchFromOrderDate: string,
  searchToOrderDate: string,
  page?: number,
  size?: number,
  sort?: string
) => IPayload<T> | ((dispatch: any) => IPayload<T>);
