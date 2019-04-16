export const enum CompanyType {
  DAMP_PROOFER = 'DAMP_PROOFER',
  DOMESTIC = 'DOMESTIC'
}

export const enum LeadType {
  WEBSITE = 'WEBSITE',
  FRIEND = 'FRIEND'
}

export const enum Status {
  ENQUIRY = 'ENQUIRY',
  SALE = 'SALE'
}

export interface ICustomer {
  id?: number;
  companyName?: string;
  address1?: string;
  address2?: string;
  address3?: string;
  town?: string;
  postCode?: string;
  title?: string;
  firstName?: string;
  lastName?: string;
  tel?: string;
  mobile?: string;
  email?: string;
  products?: string;
  interested?: string;
  paid?: number;
  type?: CompanyType;
  notes?: any;
  lead?: LeadType;
  leadName?: string;
  leadTel?: string;
  leadMob?: string;
  status?: Status;
  enquiryProperty?: string;
  enquiryUnitPq?: string;
  enquiryInstPq?: string;
  saleProducts?: string;
  saleInvoiceDate?: string;
  saleInvoiceNumber?: string;
  saleInvoiceAmount?: string;
}

export const defaultValue: Readonly<ICustomer> = {};
