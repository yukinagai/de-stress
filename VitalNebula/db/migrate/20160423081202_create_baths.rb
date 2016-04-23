class CreateBaths < ActiveRecord::Migration
  def change
    create_table :baths do |t|
      t.string :sensor
      t.float  :value
      t.timestamps null: false
    end
  end
end
